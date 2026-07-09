console.log("Employer JS Loaded");
document.getElementById("register-employer").addEventListener("click", async function (e) {

    console.log("Register Button Clicked");
    e.preventDefault();
    debugger
    const request = {
        companyName: document.getElementById("employer-companyname").value,
        companyEmail: document.getElementById("employer-companyemail").value,
        companyWebsite: document.getElementById("employer-companywebsite").value,
        HrName: document.getElementById("employer-hrname").value,
        username: document.getElementById("employer-username").value,
        password: document.getElementById("employer-password").value
    };

    console.log(request);

    // const response = await fetch("http://localhost:8080/employer/register", {

    //     method: "POST",

    //     headers: {
    //         "Content-Type": "application/json"
    //     },

    //     body: JSON.stringify(request)

    // });
    // console.log(response);

    // if(response.ok){
    //     alert("Candidate Registered Successfully");
    //     window.location.href="login.html";
    // }else{
    //     alert("Registration Failed");
    // }

    const response = await fetch("http://localhost:8080/employer/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });
    
    const data = await response.json();
    
    console.log(data);
    
    if (response.ok) {
    
        alert(data.message);
    
        setTimeout(() => {
            window.location.href = "login.html";
        }, 1000);
    
    } else {
    
        alert(data.message);
    
    }
    

    

});