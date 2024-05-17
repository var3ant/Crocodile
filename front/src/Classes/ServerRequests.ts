export class ServerRequests {
    public static async login(text: string): Promise<string | null> {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: ""
        };
        const response = await fetch('http://localhost:8080/auth/signup/' + text, requestOptions);
        if (response.ok) {
            return await response.text();
        } else {
            alert("status: " + response.status)
            return null;
        }
    }

    // public static async createRoom(text: string): Promise<string> {
    //     const requestOptions = {
    //         method: 'POST',
    //         headers: {'Content-Type': 'application/json'},
    //         body: ""
    //     };
    //
    //     const response = await fetch('http://localhost:8080/room/' + text, requestOptions);
    //     const roomId = await response.text();
    //     return roomId;
    // }
}