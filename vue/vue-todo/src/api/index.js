import axios from 'axios'; // 경로없으면 node_modules에서 들고옴

const config = {
    baseUrl: 'https://api.hnpwa.com/v0/',
};

function fetchNewsList() {
    return axios.get(`${config.baseUrl}news/1.json`);
}

async function fetchAskList() {
    try {
        return await axios.get(`${config.baseUrl}ask/1.json`);
    } catch (error) {
        console.log(error);
    }
}

function fetchJobsList() {
    return axios.get(`${config.baseUrl}jobs/1.json`);
}

// List 통일
async function fetchList(pageName) {
    try {
        return await axios.get(`${config.baseUrl}${pageName}/1.json`);
    } catch (error) {
        console.error(error);
    }
}

function fetchUserInfo(username) {
    return axios.get(`${config.baseUrl}user/${username}.json`);
}

function fetchCommentItem(id) {
    return axios.get(`${config.baseUrl}item/${id}.json`);
}

export { fetchNewsList, fetchAskList, fetchJobsList, fetchUserInfo, fetchCommentItem, fetchList };
