// =======================
// 共通
// =======================
document.addEventListener("DOMContentLoaded", () => {
	initCommon();
	
	getUsers();
	createUser();
	deleteUser();
	
	initNewSkillPage()
	getSkills();
	createSkill();
	deleteSkill();
	addSkillButtonHandler()
});

// ヘッダー及びフッター
function initCommon(){
	loadHtml("header", "./commons/header.html");
}

function loadHtml(id, path) {
	fetch(path)
	  .then(res => res.text())
	  .then(html => {
		document.getElementById(id).innerHTML = html;
	  });
}

// =======================
// ユーザー
// =======================

// ユーザー一覧取得
function getUsers(){
	const btn = document.getElementById("getUsersButton");
	
	if(!btn) return;
	
	btn.addEventListener("click", () => {
		fetch("http://localhost:8080/api/users")
		.then(response => response.json())
		.then(users => {
			const pattern = document.getElementById("userNameFilter").value;
			const tbody = document.getElementById("userTableBody");
			tbody.innerHTML = "";
			
			const filteredUsers = filterUsersByName(users, pattern);
			
			filteredUsers.forEach(user => {
				const tr = document.createElement("tr");
				tr.className = "list-row";
				
				tr.innerHTML = `
					<td>${user.id}</td>
					<td>${user.name}</td>
					<td>
						<button 
							class="btn btn-positive btn--sm add-skill-btn" 
							data-id="${user.id}">
							スキル登録
						</button>
					</td>
					<td>
						<button 
							class="btn btn-negative btn--sm delete-user-btn" 
							data-id="${user.id}">
							削除
						</button>
					</td>
				`;	// 引数はDTOクラスのフィールド名
				tbody.appendChild(tr);
			})
	
		})
	});
}

// ユーザー登録
function createUser(){
	const btn = document.getElementById("createUserButton")
	
	if(!btn) return;
	
	btn.addEventListener("click", () => {
		const name = document.getElementById("userNameInput").value;
		
		fetch("http://localhost:8080/api/users", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				name: name
			})
		})
		.then(response => {
		  if (!response.ok) {
		    return response.text().then(msg => {
		      throw new Error(msg);
		    });
		  }
		})
		.then(() => {
			// 入力欄クリア
			document.getElementById("userNameInput").value = "";
		})
		.then(() => {
			alert("登録成功");
			location.href = "/";
		})
		.catch(error => {
		  alert(error.message);
		});
	});
}

// ユーザー一覧の行を削除
function deleteUser(){
	const table = document.getElementById("userTableBody");
	
	if(!table) return
	
	table.addEventListener("click", (e) => {
	    if (!e.target.classList.contains("delete-user-btn")) return;
	
	    const id = e.target.dataset.id;
	
	    fetch(`http://localhost:8080/api/users/${id}`, {
	        method: "DELETE"
	    })
	    .then(response => {
	        if (!response.ok) {
	            return response.text().then(msg => {
	                throw new Error(msg);
	            });
	        }
	    })
	    .then(() => {
	        // 再取得
	        document.getElementById("getUsersButton").click();
	    })
	    .catch(err => alert(err.message));
	});
}

// スキル追加画面へ遷移
function addSkillButtonHandler(){
	const table = document.getElementById("userTableBody");
	
	if(!table) return
	
	table.addEventListener("click", (e) => {
	    if (!e.target.classList.contains("add-skill-btn")) return;
	
	    const id = e.target.dataset.id;
	
		location.href = `newSkill.html?userId=${id}`;
	});
}

// ユーザー名でユーザー一覧を絞り込み
function filterUsersByName(users, pattern){
	try{
		const regex = new RegExp(pattern);			
		return users.filter(user => regex.test(user.name));
	}catch{
		// 無効な正規表現の場合、全てを返す
		return users;
	}
}


// =======================
// スキル
// =======================

let skills = [];
let currentSortColumn = null;
let currentSortOrder = null;
let selectedUserId = null;



// スキル登録画面
function initNewSkillPage() {
	const userNameDiv = document.getElementById("userName");
	if (!userNameDiv) return;

	const params = new URLSearchParams(window.location.search);
	selectedUserId = params.get("userId");

	if (!selectedUserId) {
		userNameDiv.textContent = "ユーザーが指定されていません";
		return;
	}

	loadUser(selectedUserId);
}


function loadUser(userId) {
	fetch(`http://localhost:8080/api/users/${userId}`)
		.then(response => response.json())
		.then(user => {
			document.getElementById("userName").textContent = user.name;
		})
		.catch(err => {
			document.getElementById("userName").textContent = "取得失敗";
		});
}


// スキル一覧取得
function getSkills(){
	const btn = document.getElementById("getSkillsButton");
	
	if(!btn) return;
	
	btn.addEventListener("click", () => {
		fetch("http://localhost:8080/api/skills")
		.then(response => response.json())
		.then(data => {		
			const skillNameFilter = document.getElementById("skillNameFilter").value;
			const userNameFilter = document.getElementById("skillUserNameFilter").value;
	
			skills = filterSkillsBySkillNameAndUserName(data, skillNameFilter, userNameFilter);
			currentSortColumn = null;
			currentSortOrder = null;
			
			inactiveSortIcons()
			renderTable();
		});
	});
}

// テーブル作成
function renderTable() {
    const tbody = document.getElementById("skillTableBody");
    tbody.innerHTML = "";

    let displaySkills = [...skills];

    if (currentSortColumn && currentSortOrder) {
        displaySkills.sort((a, b) => {
            const compare = a[currentSortColumn]
                .localeCompare(b[currentSortColumn], "ja");

            return currentSortOrder === "asc"
                ? compare
                : -compare;
        });
    }

    displaySkills.forEach(skill => {
		const tr = document.createElement("tr");
		tr.className = "list-row";				
		tr.innerHTML = `
			<td>${skill.userName}</td>
			<td>${skill.skill}</td>
			<td>
				<button 
					class="btn btn-negative btn--sm delete-skill-btn" 
					data-id="${skill.id}">
					削除
				</button>
			</td>
		`;
		tbody.appendChild(tr);
    });
}

// ソートアイコンクリック処理
document.querySelectorAll(".sort-asc").forEach(icon => {
    icon.addEventListener("click", function (e) {

        const th = this.closest("th");
        currentSortColumn = th.dataset.column;
        currentSortOrder = "asc";

        updateSortIcons(th, "asc");
        renderTable();
    });
});

document.querySelectorAll(".sort-desc").forEach(icon => {
    icon.addEventListener("click", function (e) {
        e.stopPropagation();

        const th = this.closest("th");
        currentSortColumn = th.dataset.column;
        currentSortOrder = "desc";

        updateSortIcons(th, "desc");
        renderTable();
    });
});


// アイコン状態更新
function updateSortIcons(activeTh, order) {

	inactiveSortIcons()
	
    // 対象列のアイコンだけ有効化
    if (order === "asc") {
        activeTh.querySelector(".sort-asc")
            .classList.add("active");
    } else {
        activeTh.querySelector(".sort-desc")
            .classList.add("active");
    }
}

// 全アイコンのactive削除
function inactiveSortIcons(){
	document.querySelectorAll(".sort-asc, .sort-desc")
	    .forEach(icon => icon.classList.remove("active"));
}

// スキル登録
function createSkill(){
	const btn = document.getElementById("createSkillButton");
	if(!btn) return;

	btn.addEventListener("click", () => {

		const skill = document.getElementById("skillNameInput").value;
		const errorMsgId = "errorMessageSkill";

		hideError(errorMsgId);

		fetch("http://localhost:8080/api/skills", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				userId: Number(selectedUserId),
				skill: skill
			})
		})
		.then(response => {
		    if (!response.ok) {
		        return response.text().then(message => {
		            throw new Error(message);
		        });
		    }
		})
		.then(() => {
			document.getElementById("skillNameInput").value = "";
			alert("登録成功");
			location.href = "skills.html";
		})
		.catch(error => {
			showError(error.message, errorMsgId);
		});
	});
}



// スキル一覧の行を削除
function deleteSkill(){
	const table = document.getElementById("skillTableBody");
	
	if(!table) return;
	
	table.addEventListener("click", (e) => {
	    if (!e.target.classList.contains("delete-skill-btn")) return;
	
	    const id = e.target.dataset.id;
	
	    /*if (!confirm("本当に削除しますか？")) return;*/
	
	    fetch(`http://localhost:8080/api/skills/${id}`, {
	        method: "DELETE"
	    })
	    .then(response => {
	        if (!response.ok) {
	            return response.text().then(msg => {
	                throw new Error(msg);
	            });
	        }
	    })
	    .then(() => {
	        // 再取得
	        document.getElementById("getSkillsButton").click();
	    })
	    .catch(err => alert(err.message));
	});
}

// エラー表示
function showError(message, id){
	const elem = document.getElementById(id);
	elem.textContent = message;
	elem.classList.add("is-visible");
}

// エラー削除
function hideError(id){
	const elem = document.getElementById(id);
	elem.classList.remove("is-visible");
}

//ユーザー名およびスキル名でスキル一覧を絞り込み
function filterSkillsBySkillNameAndUserName(skills, skillName, userName){
	try{
		const skillRegex = new RegExp(skillName);
		const userNameRegex = new RegExp(userName);
		return skills.filter(skill => 
			skillRegex.test(skill.skill) &&
			userNameRegex.test(skill.userName)
		);
	}catch{
		// 無効な正規表現の場合、全てを返す
		return skills;
	}
}


