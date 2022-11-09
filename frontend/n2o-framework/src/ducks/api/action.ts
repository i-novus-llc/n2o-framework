import { takeEvery } from 'redux-saga/effects'

import { creator as switchCreator, effect as switchEffect } from './action/switch'
import { creator as conditionCreator, effect as conditionEffect } from './action/condition'
import { creator as sequenceCreator, effect as sequenceEffect } from './action/sequence'
import { EffectWrapper } from './utils/effectWrapper'

export const sagas = [
    takeEvery(conditionCreator.type, EffectWrapper(conditionEffect)),
    takeEvery(switchCreator.type, EffectWrapper(switchEffect)),
    takeEvery(sequenceCreator.type, EffectWrapper(sequenceEffect)),
]
