alert("Hi there  123");
function mandyMoore() {
    var all = document.getElementsByTagName("A");
    if (!all) {
        return;
    }

    var foundA;

    for (var i = 0; i < all.length; i++) {
        var a = all[i];
        if (a.getAttribute("class") != "uiLinkSubtle") {
            continue;
        }
        href = a.getAttribute("href");
        if (!href || href.indexOf("2380115072584") < 0) {
            continue;
        }
        foundA = a;
        break;
    }

    if (!foundA) {
        return;
    }
}

mandyMoore();
