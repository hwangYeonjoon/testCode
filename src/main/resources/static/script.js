let socket;
let username;
let room;

function enterChat() {
    username = document.getElementById("username").value;
    room = document.getElementById("room").value;
    if (username.trim() === "" || room.trim() === "") {
        alert("Please enter a valid username and room name.");
        return;
    }

    console.log(`Entering chat as ${username} in room ${room}`);

    document.getElementById("currentUser").textContent = username;
    document.getElementById("currentRoom").textContent = room;
    document.getElementById("login").style.display = "none";
    document.getElementById("chatroom").style.display = "block";
    document.getElementById("chatUI").style.display = "block";

    socket = new WebSocket("ws://localhost:8080/ws");

    socket.onopen = function() {
        console.log("WebSocket connection established");
        socket.send("JOIN:" + room);
        socket.send("SET_ID:" + username);
    };

    socket.onmessage = function(event) {
        let messagesDiv = document.getElementById("messages");
        let message = event.data;

        console.log("Received message:", message);

        if (message.startsWith("SET_ID:")) {
            console.log("Client ID set to: " + username);
        } else {
            let now = new Date();
            let timestamp = now.toLocaleString();
            let parts = message.split(":");
            let sender = parts[0];
            let content = parts[1];
            messagesDiv.innerHTML += `<div class="message"><strong>${sender}:</strong> ${content} <span class="timestamp">${timestamp}</span></div>`;
            messagesDiv.scrollTop = messagesDiv.scrollHeight;

            // 자신이 보낸 메시지가 아닌 경우에만 알림 배너 표시
            if (sender !== username) {
                showNotification(`New message from ${sender}: ${content}`);
            }
        }
    };

    socket.onclose = function(event) {
        let messagesDiv = document.getElementById("messages");
        messagesDiv.innerHTML += `<div class="message"><strong>${username} left the chat.</strong></div>`;
    };

    socket.onerror = function(error) {
        console.log("WebSocket error: ", error);
    };
}

function sendMessage() {
    let message = document.getElementById("message").value;

    if (message.trim() === "") {
        alert("Message cannot be empty.");
        return;
    }

    fetch("/chat/sendToRoom?roomId=" + room + "&senderId=" + username + "&message=" + message, {
        method: "POST"
    }).then(response => response.text())
        .then(data => console.log(data));
}

function showNotification(message) {
    let notification = document.getElementById("notification");
    notification.textContent = message;
    notification.classList.add("show");
    setTimeout(() => {
        notification.classList.remove("show");
    }, 3000);
}

// 엔터키로 입력시 채팅방 입장
document.getElementById("username").addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
        enterChat();
    }
});

document.getElementById("room").addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
        enterChat();
    }
});

document.getElementById("message").addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
});
