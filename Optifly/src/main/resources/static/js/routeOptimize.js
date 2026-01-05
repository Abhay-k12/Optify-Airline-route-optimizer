async function getOptimizedPath() {
    const source = document.getElementById('source').value.trim();
    const destination = document.getElementById('destination').value.trim();
    const priority = document.getElementById('priority').value;
    const output = document.getElementById('output');

    if (!source || !destination) {
        output.innerHTML = '<div style="color: red;">Please enter both source and destination.</div>';
        return;
    }

    const requestData = {
        source: source,
        destination: destination,
        priority: priority
    };

    try {
        const response = await fetch('/api/route/optimize', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        const result = await response.text();

        if (response.ok) {
            const formattedResult = result.replace(/\n/g, '<br>');
            output.innerHTML = `<div style="background: #f8f9fa; padding: 15px; border-radius: 5px; border-left: 4px solid #007bff;">${formattedResult}</div>`;
        } else {
            output.innerHTML = `<div style="color: red;">${result}</div>`;
        }
    } catch (error) {
        output.innerHTML = `<div style="color: red;">Error: ${error.message}</div>`;
    }
}