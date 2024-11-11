# Usage

Open developer tools in your browser and paste the following code in the console:

```javascript
let clock = document.querySelector("#table_header > span")

setInterval( () => {
fetch('http://127.0.0.1:9999', {
method: 'POST',
headers: {
'Content-Type': 'application/json'
},
body: JSON.stringify({ 'timeout': parseInt(clock.innerText) })
})
.then(response => response.text())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
}, 1000)
```