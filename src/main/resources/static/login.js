<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Smart Poultry Farm Management - Login</title>

    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
        rel="stylesheet">
</head>

<body class="bg-light">document.addEventListener("DOMContentLoaded", function () {

    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", function (event) {

        event.preventDefault();

        const username =
            document.getElementById("username").value.trim();

        const password =
            document.getElementById("password").value.trim();

        const message =
            document.getElementById("message");


        // Check empty fields

        if (username === "" || password === "") {

            message.innerHTML = `
                <div class="alert alert-danger">
                    Please enter username and password.
                </div>
            `;

            return;
        }


        // Temporary login credentials

        if (username === "admin" && password === "1234") {

            localStorage.setItem("username", username);

            message.innerHTML = `
                <div class="alert alert-success">
                    Login successful! Opening dashboard...
                </div>
            `;

            setTimeout(function () {

                window.location.href = "dashboard.html";

            }, 500);

        } else {

            message.innerHTML = `
                <div class="alert alert-danger">
                    Invalid username or password.
                </div>
            `;

        }

    });

});

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-6">

            <div class="card shadow">

                <div class="card-body p-4">

                    <h2 class="text-center mb-4">
                        Smart Poultry Farm Management
                    </h2>

                    <h4 class="text-center mb-4">
                        Login
                    </h4>

                    <form id="loginForm">

                        <div class="mb-3">

                            <label for="username" class="form-label">
                                Username
                            </label>

                            <input
                                type="text"
                                id="username"
                                class="form-control"
                                placeholder="Enter username">

                        </div>

                        <div class="mb-3">

                            <label for="password" class="form-label">
                                Password
                            </label>

                            <input
                                type="password"
                                id="password"
                                class="form-control"
                                placeholder="Enter password">

                        </div>

                        <button
                            type="submit"
                            class="btn btn-primary w-100">

                            Login

                        </button>

                    </form>

                    <div id="message" class="mt-3"></div>

                </div>

            </div>

        </div>

    </div>

</div>	

<script src="js/login.js"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

</body>

</html>