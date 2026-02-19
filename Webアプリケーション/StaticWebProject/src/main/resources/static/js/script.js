// 初期化sts
document.addEventListener("DOMContentLoaded", () => {
	initCommon();
	initForm();
	initTable();
});


// ヘッダー及びフッター
function initCommon(){
	loadHtml("header", "./commons/header.html");
	loadHtml("footer", "./commons/footer.html");
}

function loadHtml(id, path) {
	fetch(path)
	  .then(res => res.text())
	  .then(html => {
		document.getElementById(id).innerHTML = html;
	  });
}

// フォームの送信ボタン
function initForm(){
	const form = document.getElementById("js-form");
	if(!form) return;

	form.addEventListener("click", (e) => {
	// type="submit"以外の要素を無視する。
	if (e.target.type !== "submit") return;
	
	// ページ遷移阻止
	e.preventDefault();

	const group = e.target.closest(".form__group");
	const display = group.querySelector(".form__display");
	const value = getGroupValue(group);
	
	display.textContent = `➡　${value}`;
	});

	// 各input要素に対応したvalueを返す。
	function getGroupValue(group) {
	const text = group.querySelector('input[type="text"]');
	if (text) return text.value;

	const radio = group.querySelector('input[type="radio"]:checked');
	if (radio) return getFormLabel(radio.name, radio.value);

	const select = group.querySelector("select");
	if (select) return select.selectedOptions[0].text;

	const checks = group.querySelectorAll('input[type="checkbox"]:checked');
	if (checks.length) {
		return [...checks].map(c => getFormLabel(c.name, c.value)).join("、");
	}

	const textarea = group.querySelector("textarea");
	if (textarea) return textarea.value;

	return "";
	}
}

//formのvalueを日本語に変換
const labelMaps = {
  gender: {
    male: "男性",
    female: "女性",
    others: "その他",
  },
  fruits: {
    lemon: "レモン",
    apple: "りんご",
    peach: "もも",
  },
};

function getFormLabel(name, value) {
  return labelMaps[name]?.[value] ?? "";
}

// テーブル
function initTable(){
	const variableTbody = document.getElementById("js-variable__tbody");
	if(!variableTbody) return;
	let countRow = 2;

	// 初期テーブル作成
	createRow(3);

	// 追加ボタンクリック
	document.getElementById("js-variable__add-row").addEventListener("click", () => {
		createRow(1);	
	});

	// 削除ボタンクリック
	document.getElementById("js-variable__remove-row").addEventListener("click", () => {
		variableTbody.deleteRow(variableTbody.rows.length - 1);
		if(countRow != 1){
			countRow -=1;
		}
	});

	// rowを引数文作る。
	function createRow(num){
		for (let i = 0;  i < num; i++){
			const tr = document.createElement("tr");
			tr.innerHTML = `
			<td class="variable__td-index">${countRow}</td>
			<td class="variable__td-content"</td>
			`;
			variableTbody.appendChild(tr);
			countRow += 1;
		}
	}
}




