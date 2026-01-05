async function updateFlight(event) {
    event.preventDefault();
    const output = document.getElementById('output');
    
    const flightData = {
        flightId: parseInt(document.getElementById('flightId').value.trim()),
        source: document.getElementById('source').value.trim(),
        destination: document.getElementById('destination').value.trim(),
        flightDate: document.getElementById('date').value.trim() ? document.getElementById('date').value.trim() + "T00:00:00" : null,
        landingDate: document.getElementById('landingDate').value.trim() ? document.getElementById('landingDate').value.trim() + "T00:00:00" : null,
        ecoClCost: document.getElementById('ecoCost').value.trim() ? parseFloat(document.getElementById('ecoCost').value.trim()) : null,
        bussiClCost: document.getElementById('businessCost').value.trim() ? parseFloat(document.getElementById('businessCost').value.trim()) : null,
        firstClCost: document.getElementById('firstCost').value.trim() ? parseFloat(document.getElementById('firstCost').value.trim()) : null
    };

    // Remove null values for partial updates
    Object.keys(flightData).forEach(key => {
        if (flightData[key] === null || flightData[key] === '') {
            delete flightData[key];
        }
    });

    try {
        const response = await fetch(`/api/flights/${flightData.flightId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(flightData)
        });

        const result = await response.text();

        if (response.ok) {
            output.innerHTML = `<div style="color: green;">${result}</div>`;
            document.getElementById('updateFlightForm').reset();
        } else {
            output.innerHTML = `<div style="color: red;">${result}</div>`;
        }
    } catch (error) {
        output.innerHTML = `<div style="color: red;">Error: ${error.message}</div>`;
    }
}