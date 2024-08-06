// import axios from 'axios';
// import {BehaviorSubject} from 'rxjs';

// const API_URL = 'http://localhost:8765/user/service/';

// const currentUserSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')));
// class UserService {
//   get currentUserValue(){
//     return currentUserSubject.value;
//   }

//   get currentUser(){
//     return currentUserSubject.asObservable();
//   }

//   login(user){
//     const headers = {
//       authorization:'Basic ' + btoa(user.username + ':' + user.password)
//     };
//     return axios.get(API_URL + 'login', {headers: headers}).then(response => {
//       localStorage.setItem('currentUser', JSON.stringify(response.data));
//       currentUserSubject.next(response.data);
//     });
//   }

//   logOut(){
//     return axios.post(API_URL + 'logout', {}).then(response => {
//       localStorage.removeItem('currentUser');
//       currentUserSubject.next(null);
//     })
//   }

//   register(user){
//     return axios.post(API_URL + 'registration', JSON.stringify(user),
//   {headers: {'Content-Type':'application/json; charset=UTF-8'}});
//   }

// }

// export default new UserService();


// import axios from 'axios';
// import { BehaviorSubject } from 'rxjs';

// const API_URL = 'http://localhost:8765/user/service/';

// const currentUserSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')));

// class UserService {
//   get currentUserValue() {
//     return currentUserSubject.value;
//   }

//   get currentUser() {
//     return currentUserSubject.asObservable();
//   }

//   login(user) {
//     const headers = {
//       authorization: 'Basic ' + btoa(user.username + ':' + user.password)
//     };
//     return axios.get(API_URL + 'login', { headers: headers }).then(response => {
//       console.log('Login user:', user.id);
//       console.log('Login response:', response.data);
//       localStorage.setItem('currentUser', JSON.stringify(response.data));
//       currentUserSubject.next(response.data);
//       return response.data; // Ensure login returns data for further use if needed
//     });
//   }

//   logOut() {
//     return axios.post(API_URL + 'logout', {}).then(response => {
//       localStorage.removeItem('currentUser');
//       currentUserSubject.next(null);
//       return response.data; // Ensure logout returns data if needed
//     });
//   }

//   register(user) {
//     return axios.post(API_URL + 'registration', JSON.stringify(user), {
//       headers: { 'Content-Type': 'application/json; charset=UTF-8' },
//     });
//   }
// }

// export default new UserService();

import axios from 'axios';
import { BehaviorSubject } from 'rxjs';

const API_URL = 'http://localhost:8765/user/service/';

const currentUserSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')));

class UserService {
  get currentUserValue() {
    return currentUserSubject.value;
  }

  get currentUser() {
    return currentUserSubject.asObservable();
  }

  async login(user) {
    const headers = {
      authorization: 'Basic ' + btoa(user.username + ':' + user.password)
    };
    try {
      const response = await axios.post(API_URL + 'login', user);
      
      console.log('Login response:', response.data);
      if (response.status === 200) {
        localStorage.setItem('currentUser', JSON.stringify(response.data));
        currentUserSubject.next(response.data);
      } else {
        console.error('Unexpected response status:', response.status);
      }
      return response.data;
    } catch (error) {
      console.error('Login error:', error);
      throw error; // Re-throw the error to handle it elsewhere if needed
    }
  }

  async logOut() {
    try {
      const response = await axios.post(API_URL + 'logout', {});
      localStorage.removeItem('currentUser');
      currentUserSubject.next(null);
      return response.data;
    } catch (error) {
      console.error('Logout error:', error);
      throw error; // Re-throw the error to handle it elsewhere if needed
    }
  }
  

  register(user) {
    return axios.post(API_URL + 'registration', JSON.stringify(user), {
      headers: { 'Content-Type': 'application/json; charset=UTF-8' }
    });
  }
}

export default new UserService();
