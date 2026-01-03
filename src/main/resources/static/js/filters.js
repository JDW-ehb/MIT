const searchBox = document.getElementById("searchBox");
const userFilter = document.getElementById("userFilter");
const statusFilter = document.getElementById("statusFilter");
const dateFilter = document.getElementById("dateFilter");

function applyFilters() {

    const searchTerm = searchBox.value.toLowerCase();
    const userTerm = userFilter.value.toLowerCase();
    const statusTerm = statusFilter.value;
    const dateTerm = dateFilter.value;   // yyyy-mm-dd

    document
        .querySelectorAll(".user-card")
        .forEach(card => {

            const username = card
                .querySelector(".username")
                .textContent.toLowerCase();

            // --- USER FILTER ---
            if (userTerm && !username.includes(userTerm)) {
                card.style.display = "none";
                return;
            }

            let anyVisibleObjective = false;

            card.querySelectorAll(".objective").forEach(obj => {

                const text = obj.textContent.toLowerCase();
                const status = obj.dataset.status;
                const date = obj.dataset.date;

                let show = true;

                // search filter
                if (searchTerm && !text.includes(searchTerm)) show = false;

                // status filter
                if (statusTerm && status !== statusTerm) show = false;

                // date filter
                if (dateTerm && date !== dateTerm) show = false;

                obj.style.display = show ? "flex" : "none";

                if (show) anyVisibleObjective = true;
            });

            // hide user card if no visible objectives
            card.style.display = anyVisibleObjective ? "block" : "none";
        });
}

// Listen for changes
[searchBox, userFilter, statusFilter, dateFilter]
    .forEach(input => input.addEventListener("input", applyFilters));
