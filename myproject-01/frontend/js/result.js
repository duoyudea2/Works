window.onload = function () {
    const data = JSON.parse(sessionStorage.getItem("fortuneResult"));
    if (!data) {
        window.location.href = "fortune.html";
        return;
    }

    const f = data.fortune;
    const lvl = f.fortuneLevel;
    const lvlText = lvl === "good" ? "上签" : (lvl === "medium" ? "中签" : "下签");

    document.getElementById("subtitle").innerText = "关注领域：" + data.interests;
    document.getElementById("level").innerText = `签诗 #${f.id} · ${lvlText}`;
    document.getElementById("level").className = "level " + lvl;
    document.getElementById("poem").innerText = f.poem;

    document.getElementById("interpretation").innerText = data.interpretation;
    document.getElementById("text-zh").innerText = data.interpretation;
    document.getElementById("text-en").innerText = data.english;
    document.getElementById("text-jp").innerText = data.japanese;
};

function switchTab(e, lang) {
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.tabs button').forEach(el => el.classList.remove('active'));
    document.getElementById('tab-' + lang).classList.add('active');
    e.target.classList.add('active');
}