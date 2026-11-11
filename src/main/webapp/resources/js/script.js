function copyToClipboard(id) {
    const element = document.getElementById('form:' + id);
    if (element) {
        navigator.clipboard.writeText(element.value);
    }
}

function toutEffacer() {
    const q = document.getElementById('form:question');
    const r = document.getElementById('form:reponse');
    if (q) q.value = '';
    if (r) r.value = '';
}