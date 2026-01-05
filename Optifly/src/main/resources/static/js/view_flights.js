async function viewFlight(event) {
    event.preventDefault();
    const output = document.getElementById('output');
    
    const flightId = parseInt(document.getElementById('flightId').value.trim());

    if (!flightId) {
        output.innerHTML = '<div style="color: red; margin-top: 20px;">Please enter a valid Flight ID</div>';
        return;
    }

    try {
        const response = await fetch(`/api/flights/${flightId}`);

        if (response.ok) {
            const flight = await response.json();

            const formattedInfo = `
                <div>
                    <div><strong>Flight ID:</strong> ${flight.flightId}</div>
                    <div><strong>Source:</strong> ${flight.source.toUpperCase()}</div>
                    <div><strong>Destination:</strong> ${flight.destination.toUpperCase()}</div>
                    <div><strong>Departure:</strong> ${formatDateTime(flight.flightDate)}</div>
                    <div><strong>Arrival:</strong> ${formatDateTime(flight.landingDate)}</div>
                    <div><strong>Economy Class:</strong> ₹${flight.ecoClCost}</div>
                    <div><strong>Business Class:</strong> ₹${flight.bussiClCost}</div>
                    <div><strong>First Class:</strong> ₹${flight.firstClCost}</div>
                </div>
            `;

            output.innerHTML = formattedInfo;
        } else if (response.status === 404) {
            output.innerHTML = '<div style="color: red; margin-top: 20px;">Flight not found. Please check the Flight ID.</div>';
        } else {
            const error = await response.text();
            output.innerHTML = `<div style="color: red; margin-top: 20px;">${error}</div>`;
        }
    } catch (error) {
        output.innerHTML = `<div style="color: red; margin-top: 20px;">Error: ${error.message}</div>`;
    }
}

function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return 'N/A';
    try {
        const date = new Date(dateTimeStr);
        return date.toLocaleString('en-IN', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    } catch (e) {
        return dateTimeStr;
    }
}