async function addFlight(event) {
    event.preventDefault();
    const output = document.getElementById('output');

    const flightData = {
        source: document.getElementById('source').value.trim(),
        destination: document.getElementById('destination').value.trim(),
        flightDate: document.getElementById('date').value.trim() + 'T00:00:00', // Add time for DATETIME format
        landingDate: document.getElementById('landingDate').value.trim() + 'T00:00:00', // Add time for DATETIME format
        ecoCost: parseFloat(document.getElementById('ecoCost').value.trim()) || 0,
        businessCost: parseFloat(document.getElementById('businessCost').value.trim()) || 0,
        firstCost: parseFloat(document.getElementById('firstCost').value.trim()) || 0
    };

    // Validate required fields
    if (!flightData.source || !flightData.destination || !flightData.flightDate || !flightData.landingDate) {
        output.innerHTML = '<div style="color: red;">Please fill all required fields</div>';
        return;
    }

    try {
        const response = await fetch('/api/flights', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(flightData)
        });

        const result = await response.text();

        if (response.ok) {
            output.innerHTML = `<div style="color: green;">${result}</div>`;
            document.getElementById('addFlightForm').reset();
        } else {
            output.innerHTML = `<div style="color: red;">${result}</div>`;
        }
    } catch (error) {
        output.innerHTML = `<div style="color: red;">Error: ${error.message}</div>`;
    }
}