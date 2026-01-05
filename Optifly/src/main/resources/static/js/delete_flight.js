async function deleteFlight(event) {
    event.preventDefault();
    const output = document.getElementById('output');

    const flightId = parseInt(document.getElementById('flightId').value.trim());

    if (!flightId) {
        output.innerHTML = '<div style="color: red;">Please enter a valid Flight ID</div>';
        return;
    }

    try {
        const response = await fetch(`/api/flights/${flightId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        const result = await response.text();

        if (response.ok) {
            output.innerHTML = `<div style="color: green;">${result}</div>`;
            document.getElementById('deleteFlightForm').reset();
        } else {
            output.innerHTML = `<div style="color: red;">${result}</div>`;
        }
    } catch (error) {
        output.innerHTML = `<div style="color: red;">Error: ${error.message}</div>`;
    }
}