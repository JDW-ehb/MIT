const searchBox = document.getElementById("searchBox");
const userFilter = document.getElementById("userFilter");
const statusFilter = document.getElementById("statusFilter");
const dateFilter = document.getElementById("dateFilter");
const categoryFilter = document.getElementById("categoryFilter"); // NEW

function applyFilters() {

    const searchTerm = searchBox.value.toLowerCase();
    const userTerm = userFilter.value.toLowerCase();
    const statusTerm = statusFilter.value;
    const dateTerm = dateFilter.value;
    const categoryTerm = categoryFilter ? categoryFilter.value.toLowerCase() : "";

    document
        .querySelectorAll(".user-card")
        .forEach(card => {

            const username = card
                .querySelector(".username")
                .textContent.toLowerCase();

            if (userTerm && !username.includes(userTerm)) {
                card.style.display = "none";
                return;
            }

            let anyVisibleObjective = false;

            card.querySelectorAll(".objective").forEach(obj => {

                const text = obj.textContent.toLowerCase();
                const status = obj.dataset.status;
                const date = obj.dataset.date;
                const category = (obj.dataset.category || "").toLowerCase();

                let show = true;

                if (searchTerm &&
                    !text.includes(searchTerm) &&
                    !category.includes(searchTerm))
                    show = false;

                if (statusTerm && status !== statusTerm)
                    show = false;

                if (dateTerm && date !== dateTerm)
                    show = false;

                if (categoryTerm && category !== categoryTerm)
                    show = false;

                obj.style.display = show ? "flex" : "none";

                if (show) anyVisibleObjective = true;
            });

            card.style.display = anyVisibleObjective ? "block" : "none";
        });
}

[
    searchBox,
    userFilter,
    statusFilter,
    dateFilter,
    categoryFilter
].forEach(input => input && input.addEventListener("input", applyFilters));
