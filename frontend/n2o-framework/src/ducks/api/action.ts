import { takeEvery } from 'redux-saga/effects'

import { creator as switchCreator, effect as switchEffect } from './action/switch'
import { creator as conditionCreator, effect as conditionEffect } from './action/condition'
import { creator as sequenceCreator, effect as sequenceEffect } from './action/sequence'

export const sagas = [
    takeEvery(conditionCreator.type, conditionEffect),
    takeEvery(switchCreator.type, switchEffect),
    takeEvery(sequenceCreator.type, sequenceEffect),
]
