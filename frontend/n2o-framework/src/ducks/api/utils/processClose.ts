import { put, call } from 'redux-saga/effects'

import { remove, showPrompt } from '../../overlays/store'
import { showPagePrompt } from '../../pages/store'

import { resetQuery } from './resetQuery'
import { checkOnDirtyForm } from './checkOnDirtyForm'

/**
 * Функция закрытия оверлея с учётом необходимости показа подтверждения
 * @param pageId – идентификатор страницы
 * @param prompt – нужно ли проверять dirty формы перед закрытием
 */
export function* processOverlayClose(pageId: string, prompt: boolean) {
    if (!prompt) {
        yield put(remove())
        yield call(resetQuery, pageId)

        return
    }

    const needToShowPrompt: boolean = yield call(checkOnDirtyForm, pageId)

    if (!needToShowPrompt) {
        yield put(remove())
        yield call(resetQuery, pageId)
    } else {
        yield put(showPrompt(pageId))
    }
}

/**
 * Функция закрытия рутовой страницы (вкладки) с учётом необходимости показа подтверждения
 * @param pageId – идентификатор страницы
 * @param prompt – нужно ли проверять dirty формы перед закрытием
 */
export function* processPageClose(pageId: string, prompt: boolean) {
    if (!prompt) {
        window.close()

        return
    }

    const needToShowPrompt: boolean = yield call(checkOnDirtyForm)

    if (!needToShowPrompt) {
        window.close()
    } else {
        yield put(showPagePrompt({ pageId }))
    }
}
