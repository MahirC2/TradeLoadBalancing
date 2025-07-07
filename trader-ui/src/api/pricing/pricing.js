import axios from 'axios';

export const fetchPrice = async (symbol, currency) => {
    const res = await axios.get(`${process.env.REACT_APP_API}/price`, {
        params: { symbol, currency }
    });
    return res.data;
};
