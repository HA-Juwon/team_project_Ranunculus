const searchButton = window.document.getElementById('search-button');
const searchForm=window.document.getElementById('hd-sec-search');

const myButton = window.document.getElementById('my-button');
const myMenu=window.document.getElementById('menu-container');


searchButton.addEventListener('click', () => {
    console.log("작동함");
    if(searchForm.classList.contains("visible")){
        searchForm.classList.remove("visible");
        console.log("작동함");
    }
    else {
        searchForm.classList.add("visible");
        console.log("작동함");
    }
});
myButton.addEventListener('click', () => {
    console.log("작동함");
    if(myMenu.classList.contains("visible")){
        myMenu.classList.remove("visible");
        console.log("작동함");
    }
    else {
        myMenu.classList.add("visible");
        console.log("작동함");
    }
});