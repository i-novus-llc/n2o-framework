import { takeEvery } from 'redux-saga/effects'

import { creator as editListCreator, effect as editListEffect } from './models/editList'
import { EffectWrapper } from './utils/effectWrapper'

export const sagas = [
    // @ts-ignore проблема с типизацией saga
    takeEvery(editListCreator.type, EffectWrapper(editListEffect)),
]
