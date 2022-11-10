import { sagas as actionsSagas } from './action'
// @ts-ignore ignore import error from js file
import { sagas as alertSagas } from './alerts'
import { sagas as modelsSagas } from './models'

export const sagas = [
    ...actionsSagas,
    ...alertSagas,
    ...modelsSagas,
]
