import { sagas as actionsSagas } from './action'
// @ts-ignore ignore import error from js file
import { sagas as alertSagas } from './alerts'
import { sagas as modelsSagas } from './models'
import { sagas as pageSagas } from './page'
import { sagas as exportSagas } from './export'

export const sagas = [
    ...actionsSagas,
    ...alertSagas,
    ...modelsSagas,
    ...pageSagas,
    ...exportSagas,
]
