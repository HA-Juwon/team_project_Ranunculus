const searchButton = window.document.getElementById('search-button');
const searchForm = window.document.getElementById('hd-sec-search');
const myButton = window.document.getElementById('my-button');
const myMenu = window.document.getElementById('menu-container');
const body = document.getElementsByTagName('body')[0];

HTMLInputElement.prototype.focusAndSelect = function () {
    this.focus();
    this.select();
}

const cover = {
    getElement: () => window.document.body.querySelector(':scope > .loading'),
    show: () => {
        body.classList.add('scrollLock');
        const coverElementDiv = window.document.createElement('div');
        coverElementDiv.classList.add('loading');
        const coverElementDivTwo = window.document.createElement('div');
        coverElementDivTwo.classList.add('progress');
        const addImg = window.document.createElement('img');
        addImg.classList.add('image');
        addImg.setAttribute('src', '/images/loading.progress.image.png');
        const coverElementA = window.document.createElement('span');
        coverElementA.classList.add('myFont');
        coverElementA.innerText = '잠시만 기다려 주세요.';
        coverElementDiv.append(coverElementDivTwo);
        coverElementDiv.append(coverElementA);
        coverElementDivTwo.append(addImg);
        window.document.body.append(coverElementDiv);
        cover.getElement().classList.add('visible');

    },
    hide: () => {
        cover.getElement().classList.remove('visible');
        body.classList.remove('scrollLock');
    }

}


searchButton.addEventListener('click', () => {
    console.log("작동함");
    if (searchForm.classList.contains("visible")) {
        searchForm.classList.remove("visible");
        console.log("작동함");
    } else {
        searchForm.classList.add("visible");
        console.log("작동함");
    }
});
myButton.addEventListener('click', () => {
    console.log("작동함");
    if (myMenu.classList.contains("visible")) {
        myMenu.classList.remove("visible");
        console.log("작동함");
    } else {
        myMenu.classList.add("visible");
        console.log("작동함");
    }
});