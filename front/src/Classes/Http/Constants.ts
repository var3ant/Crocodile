// export const BACKEND_URL: string = 'http://localhost:8080'
// export const BACKEND_URL: string = 'http://10.242.65.3:8080'

// @ts-ignore
export const BACKEND_HOSTNAME: string = process.env.SPRING_HOSTNAME
// @ts-ignore
export const BACKEND_PORT: string = process.env.SPRING_PORT

export const BACKEND_URL: string = 'http://' + BACKEND_HOSTNAME + ":" + BACKEND_PORT;