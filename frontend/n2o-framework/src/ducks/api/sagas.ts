import { sagas as actionsSagas } from './action'
import { sagas as alertSagas } from './alerts'
import { sagas as modelsSagas } from './models'
import { sagas as pageSagas } from './page'
import { sagas as exportSagas } from './export'
import { sagas as confirmSagas } from './confirm'
import { sagas as mapParamsSagas } from './mapParams'

export const sagas = [
    ...actionsSagas,
    ...alertSagas,
    ...modelsSagas,
    ...pageSagas,
    ...exportSagas,
    ...confirmSagas,
    ...mapParamsSagas,
]
