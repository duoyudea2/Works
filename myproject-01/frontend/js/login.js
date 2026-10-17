async function doLogin() {
    const msg = document.getElementById("msg");
    msg.innerText = "";

    const data = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

    try {
        const res = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
        const json = await res.json();

        if (json.code === 200) {
            localStorage.setItem("token", json.token);
            localStorage.setItem("username", json.username);
            window.location.href = "fortune.html";
        } else {
            msg.innerText = json.msg;
        }
    } catch (e) {
        msg.innerText = "网络错误：" + e.message;
    }
}