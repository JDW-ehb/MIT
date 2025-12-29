// --- Mocked data (replace later with backend JSON) ---
const users = ["Neo","Trinity","Morpheus","Smith","Oracle"];
const statuses = ["Todo","In Progress","Done","Abandoned"];
const dates = ["2025-12-29","2025-12-30","2026-01-01"];

function setupFilter(inputId, listId, data){
    const input = document.getElementById(inputId);
    const list = document.getElementById(listId);

    function renderList(filter=""){
        list.innerHTML = "";
        data
            .filter(x => x.toLowerCase().includes(filter.toLowerCase()))
            .forEach(item=>{
                const li = document.createElement("li");
                li.textContent = item;
                li.onclick = ()=>{
                    input.value = item;
                    list.classList.add("hidden");
                }
                list.appendChild(li);
            });
    }

    input.addEventListener("focus", ()=>{
        renderList(input.value);
        list.classList.remove("hidden");
    });

    input.addEventListener("keyup", ()=>{
        renderList(input.value);
        list.classList.remove("hidden");
    });

    document.addEventListener("click", e=>{
        if(!input.parentElement.contains(e.target)){
            list.classList.add("hidden");
        }
    });
}

// initialize
setupFilter("userFilter","userList",users);
setupFilter("statusFilter","statusList",statuses);
setupFilter("dateFilter","dateList",dates);
