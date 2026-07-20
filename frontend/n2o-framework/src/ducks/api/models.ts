import { takeEvery } from 'redux-saga/effects'

import { creator as editListCreator, effect as editListEffect } from './models/editList'
import { creator as copyCreator, effect as copyEffect } from './models/copy'
import { AsyncEffectWrapper } from './utils/effectWrapper'

export const sagas = [
    // @ts-ignore проблема с типизацией saga
    takeEvery(editListCreator.type, AsyncEffectWrapper(editListEffect)),
    // @ts-ignore проблема с типизацией saga
    takeEvery(copyCreator.type, AsyncEffectWrapper(copyEffect)),
]
