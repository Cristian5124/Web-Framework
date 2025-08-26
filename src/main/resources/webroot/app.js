function testEndpoint(endpoint, resultId) {
    const resultDiv = document.getElementById(resultId);

    resultDiv.textContent = 'Loading...';
    resultDiv.className = 'result show';

    // API
    fetch(endpoint)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            return response.text();
        })
        .then(data => {

            try {
                const jsonData = JSON.parse(data);
                resultDiv.textContent = JSON.stringify(jsonData, null, 2);
            } catch (e) {

                resultDiv.textContent = data;
            }
            resultDiv.className = 'result show';
        })
        .catch(error => {
            resultDiv.textContent = `Error: ${error.message}`;
            resultDiv.className = 'result show error';
        });
}

document.addEventListener('DOMContentLoaded', function () {
    console.log('Web Framework Demo loaded');

    document.addEventListener('keydown', function (event) {
        if (event.key === 'Enter' && event.target.tagName === 'BUTTON') {
            event.target.click();
        }
    });
});
