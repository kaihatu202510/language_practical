// =======================
// 共通
// =======================
document.addEventListener("DOMContentLoaded", () => {
	
	getUsers();
	createUser();
	deleteUser();
	
	getSkills();
	createSkill();
	updateSkill();
	deleteSkill();
	updateUser();
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
		filterUser();
	});
}

// ユーザー登録
function createUser(){
	const btn = document.getElementById("createUserButton")
	
	if(!btn) return;
	const token = document.querySelector('meta[name="_csrf"]').content;
	const header = document.querySelector('meta[name="_csrf_header"]').content;
	
	btn.addEventListener("click", () => {
		const name = document.getElementById("userNameInput").value;
		const password = document.getElementById("userPasswordInput").value;
		const role = document.getElementById("userRoleInput").value;
		
		fetch("http://localhost:8080/api/users", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				[header]: token
			},
			body: JSON.stringify({
				name: name,
				password: password,
				role: role
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
			alert("登録成功");
			location.href = "/users";
		})
		.catch(error => {
		  alert(error.message);
		});
	});
}

// ユーザー更新
function updateUser(){
		const btn = document.getElementById("updateUserButton");
		
		if(!btn) return;
		
		const token = document.querySelector('meta[name="_csrf"]').content;
		const header = document.querySelector('meta[name="_csrf_header"]').content;
			
		btn.addEventListener("click", () => {
			console.log("hello")

			const name = document.getElementById("userNameInput").value;
			const role = document.getElementById("userRoleInput").value;
			const id = Number(btn.dataset.userId);

			fetch(`http://localhost:8080/api/user/update/${id}`, {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
					[header]:token
				},
				body: JSON.stringify({
					id: id,
					name: name,
					role: role
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
				alert("更新成功");
				location.href = "/users";
			})
			.catch(err => alert(err.message));
		});
	}

// ユーザー一覧の行を削除
function deleteUser(){
	const tbody = document.getElementById("userTableBody");

	if(!tbody) return;
	
	const token = document.querySelector('meta[name="_csrf"]').content;
	const header = document.querySelector('meta[name="_csrf_header"]').content;
	
	tbody.addEventListener("click", (e) => {
		if (!e.target.classList.contains("updatete-user-btn")) return;

        const button = e.target;
        const id = button.dataset.id;

        fetch("http://localhost:8080/api/user/update/" + id, {
            method: "UPDATE",
			headers:{
				[header]:token
			}
			
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(msg => {
                    throw new Error(msg);
                });
            }
        })
        .then(() => {
			const row = e.target.closest("tr");
			row.remove();
			
			// 削除後にデータが0ならデータがない旨を表示。
			const remainingRows = tbody.querySelectorAll("tr");
			if (remainingRows.length === 0) {
	            const emptyRow = document.createElement("tr");

	            emptyRow.innerHTML = `
	                <td colspan="3">データがありません</td>
	            `;

	            tbody.appendChild(emptyRow);
	        }
        })
        .catch(err => alert(err.message));
    });
}


// user検索
function filterUser() {
	const keyword = document.getElementById("userNameFilter").value;

    let regex;

    try {
        regex = new RegExp(keyword);
    } catch (e) {
        // 正規表現が壊れている場合は何もしない
        return;
    }

    const rows = document.querySelectorAll("#userTableBody tr.list-row");

    rows.forEach(row => {
        const text = row.children[1].textContent;

        if (regex.test(text)) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}


// =======================
// スキル
// =======================

let skills = [];
let currentSortColumn = null;
let currentSortOrder = null;
let selectedUserId = null;


// スキル一覧取得
function getSkills() {
    const btn = document.getElementById("getSkillsButton");
    if (!btn) return;

    btn.addEventListener("click", () => {
        filterSkills();

        if (currentSortColumn && currentSortOrder) {
            sortTable(currentSortColumn, currentSortOrder);
        }
    });
}

// スキル一覧ソート
function sortTable(columnKey, order) {

    const tbody = document.getElementById("skillTableBody");
    const rows = Array.from(tbody.querySelectorAll("tr"));

    // 列番号を決定
    let columnIndex;
    if (columnKey === "userName") {
        columnIndex = 0;
    } else if (columnKey === "skill") {
        columnIndex = 1;
    }

    rows.sort((a, b) => {
        const aText = a.children[columnIndex].textContent.trim();
        const bText = b.children[columnIndex].textContent.trim();

        return order === "asc"
            ? aText.localeCompare(bText, "ja")
            : bText.localeCompare(aText, "ja");
    });

    // 並び替えた順に再追加
    rows.forEach(row => tbody.appendChild(row));
}

// ソートアイコンクリック処理
document.addEventListener("click", function (e) {

    if (e.target.classList.contains("sort-asc") ||
        e.target.classList.contains("sort-desc")) {

        const th = e.target.closest("th");
        const sortColumn = th.dataset.column;
        const sortOrder =
            e.target.classList.contains("sort-asc") ? "asc" : "desc";
			
		currentSortColumn = sortColumn;
		currentSortOrder = sortOrder;
		
        updateSortIcons(th, sortOrder);
        sortTable(sortColumn, sortOrder);
    }
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
	
	const token = document.querySelector('meta[name="_csrf"]').content;
	const header = document.querySelector('meta[name="_csrf_header"]').content;
		
	btn.addEventListener("click", () => {

		const name = document.getElementById("skillNameInput").value;
		const userId = Number(btn.dataset.userId);
		const errorMsgId = "errorMessageSkill";

		hideError(errorMsgId);

		fetch("http://localhost:8080/api/skills", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				[header]:token
			},
			body: JSON.stringify({
				userId: userId,
				name: name
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
			alert("登録成功");
			location.href = "/skills";
		})
		.catch(error => {
			showError(error.message, errorMsgId);
		});
	});
}

// スキル更新
function updateSkill(){
	const btn = document.getElementById("updateSkillButton");
	
	if(!btn) return;
	
	const token = document.querySelector('meta[name="_csrf"]').content;
	const header = document.querySelector('meta[name="_csrf_header"]').content;
		
	btn.addEventListener("click", () => {

		const name = document.getElementById("skillNameInput").value;
		const id = Number(btn.dataset.skillId);

		fetch(`http://localhost:8080/api/skills/edit/${id}`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				[header]:token
			},
			body: JSON.stringify({
				id: id,
				name: name
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
			alert("更新成功");
			location.href = "/skills";
		})
		.catch(err => alert(err.message));
	});
}



// スキル一覧の行を削除
function deleteSkill(){
	const tbody = document.getElementById("skillTableBody");
	
	if(!tbody) return;
	
	const token = document.querySelector('meta[name="_csrf"]').content;
	const header = document.querySelector('meta[name="_csrf_header"]').content;
		
	tbody.addEventListener("click", (e) => {
	    if (!e.target.classList.contains("delete-skill-btn")) return;
	
	    const id = e.target.dataset.id;
	
	    /*if (!confirm("本当に削除しますか？")) return;*/
	
	    fetch(`http://localhost:8080/api/skills/${id}`, {
	        method: "DELETE",
			headers:{
				[header]:token
			}
	    })
	    .then(response => {
	        if (!response.ok) {
	            return response.text().then(msg => {
	                throw new Error(msg);
	            });
	        }
	    })
	    .then(() => {
			const row = e.target.closest("tr");
			row.remove();
			
			// 削除後にデータが0ならデータがない旨を表示。
			const remainingRows = tbody.querySelectorAll("tr");
			if (remainingRows.length === 0) {
	            const emptyRow = document.createElement("tr");

	            emptyRow.innerHTML = `
	                <td colspan="3">データがありません</td>
	            `;

	            tbody.appendChild(emptyRow);
	        }
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

// ユーザー名およびスキル名でスキル一覧を絞り込み
function filterSkills() {
    const userInput = document.getElementById("skillUserNameFilter");
    const skillInput = document.getElementById("skillNameFilter");

    if (!userInput || !skillInput) return;

    const userKeyword = userInput.value;
    const skillKeyword = skillInput.value;

    let userRegex, skillRegex;

    try {
        userRegex = new RegExp(userKeyword);
        skillRegex = new RegExp(skillKeyword);
    } catch (e) {
        return;
    }

    const rows = document.querySelectorAll("#skillTableBody tr");

    rows.forEach(row => {
        const userName = row.children[0].textContent;
        const skill = row.children[1].textContent;

        const match =
            userRegex.test(userName) &&
            skillRegex.test(skill);

        row.style.display = match ? "" : "none";
    });
}


