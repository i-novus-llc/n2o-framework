import { select } from 'redux-saga/effects'

import { widgetsSelector, makeWidgetsByPageIdSelector } from '../../widgets/selectors'
import { isDirtyForm } from '../../form/selectors'
import { type State as WidgetsState } from '../../widgets/Widgets'

/**
 * Проверка на изменение данных в формах
 */
export function* checkOnDirtyForm(pageId?: string) {
    let someOneDirtyForm = false

    // @INFO проверка всех форм
    if (!pageId) {
        const widgets: WidgetsState = yield select(widgetsSelector)

        for (const widgetName of Object.keys(widgets)) {
            const dirty: boolean = yield select(isDirtyForm(widgetName))

            someOneDirtyForm = someOneDirtyForm || dirty
        }

        return someOneDirtyForm
    }

    const widgets: WidgetsState = yield select(makeWidgetsByPageIdSelector(pageId))

    for (const widgetName of Object.keys(widgets)) {
        const dirty: boolean = yield select(isDirtyForm(widgetName))

        someOneDirtyForm = someOneDirtyForm || dirty
    }

    return someOneDirtyForm
}
