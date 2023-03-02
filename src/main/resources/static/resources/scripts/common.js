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
    show: (message) => {
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

        if(message === undefined) {
            coverElementA.style.display = 'none';
        } else {
            coverElementA.innerText = message;
        }

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
    if (searchForm.classList.contains("visible")) {
        searchForm.classList.remove("visible");
    } else {
        searchForm.classList.add("visible");
    }
});

myButton.addEventListener('click', () => {
    if (myMenu.classList.contains("visible")) {
        myMenu.classList.remove("visible");
    } else {
        myMenu.classList.add("visible");
    }
});

searchForm.onsubmit = e => {
    e.preventDefault();

    window.location.href = "/shop/search";
    // const
};