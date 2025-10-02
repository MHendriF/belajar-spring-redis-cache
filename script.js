import http from 'k6/http';
import { check } from 'k6';
import { Trend } from 'k6/metrics';

export const options = {
    stages: [
        { duration: '10s', target: 50 },   // ramp up to 50 VUs
        { duration: '50s', target: 100 },  // stay at 100 VUs
        { duration: '10s', target: 0 },    // ramp down to 0 VUs
    ],
    thresholds: {
        http_req_duration: ['p(95)<200'], // p95 harus < 200ms
    },
};

export default function () {
    const res = http.get('http://localhost:8080/api/categories');

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}