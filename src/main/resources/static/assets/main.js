document.addEventListener("click",s=>{const t=s.target.closest("[data-toggle]");t&&(t.classList.toggle("is-on"),t.setAttribute("aria-pressed",t.classList.contains("is-on")))});
