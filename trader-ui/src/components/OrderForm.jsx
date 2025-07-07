import React, { useState } from 'react';
import axios from 'axios';

export default function OrderForm() {
    const [symbol, setSymbol] = useState('AAPL');
    const [qty, setQty] = useState(1);
    const [response, setResponse] = useState(null);
    const [error, setError] = useState(null);

    const handleSubmit = async e => {
        e.preventDefault();
        setError(null);
        try {
            const res = await axios.post(`${process.env.REACT_APP_API}/order`, {
                symbol,
                qty
            });
            setResponse(res.data);
        } catch (err) {
            setError("Order failed: " + err.message);
        }
    };

    return (
        <div>
            <h2>Place Order</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Ticker Symbol:</label>
                    <input
                        type="text"
                        value={symbol}
                        onChange={e => setSymbol(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Quantity:</label>
                    <input
                        type="number"
                        min="1"
                        value={qty}
                        onChange={e => setQty(Number(e.target.value))}
                        required
                    />
                </div>
                <button type="submit">Submit Order</button>
            </form>

            {response && (
                <div style={{ marginTop: '1rem' }}>
                    <h3>Order Response</h3>
                    <pre>{JSON.stringify(response, null, 2)}</pre>
                </div>
            )}

            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
}
