import React, { useState } from 'react';
import { fetchPrice } from '../api/pricing/pricing';

export default function PriceChecker() {
    const [symbol, setSymbol] = useState('AAPL');
    const [currency, setCurrency] = useState('USD');
    const [price, setPrice] = useState(null);

    const handleCheck = async () => {
        const result = await fetchPrice(symbol, currency);
        setPrice(result);
    };

    return (
        <div>
            <h2>Check Price</h2>
            <input value={symbol} onChange={e => setSymbol(e.target.value)} />
            <select value={currency} onChange={e => setCurrency(e.target.value)}>
                <option value="USD">USD</option>
                <option value="GBP">GBP</option>
                <option value="JPY">JPY</option>
            </select>
            <button onClick={handleCheck}>Get Price</button>
            {price && (
                <>
                    <pre>{JSON.stringify(price, null, 2)}</pre>
                    {console.log('Price:', price)}
                </>
            )}
        </div>
    );
}
