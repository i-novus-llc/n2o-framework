import { Action } from '../Action'

export type UserLoginPayload = {
    payload: object
}

export type UserLogin = Action<string, UserLoginPayload>
