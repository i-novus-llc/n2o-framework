import { select } from 'redux-saga/effects'

import { widgetsSelector, makeWidgetsByPageIdSelector } from '../../widgets/selectors'
import { makeFormByName } from '../../form/selectors'
import { type State as WidgetsState } from '../../widgets/Widgets'
import { type Form } from '../../form/types'

/**
 * Проверка на изменение данных в формах
 */
export function* checkOnDirtyForm(pageId?: string) {
    let someOneDirtyForm = false

    // @INFO проверка всех форм
    if (!pageId) {
        const widgets: WidgetsState = yield select(widgetsSelector)

        for (const widgetName of Object.keys(widgets)) {
            const form: Form = yield select(makeFormByName(widgetName))

            someOneDirtyForm = someOneDirtyForm || form.dirty
        }

        return someOneDirtyForm
    }

    const widgets: WidgetsState = yield select(makeWidgetsByPageIdSelector(pageId))

    for (const widgetName of Object.keys(widgets)) {
        const form: Form = yield select(makeFormByName(widgetName))

        someOneDirtyForm = someOneDirtyForm || form.dirty
    }

    return someOneDirtyForm
}
