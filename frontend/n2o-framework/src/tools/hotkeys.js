import React from 'react'
import { setDisplayName, wrapDisplayName } from 'recompose'

import * as Key from './keycodes'

/**
 * Список KeyCode в формате `KEY_BACKSPACE = 8`
 * @example
 * import { KEY_UP_ARROW, KEY_DOWN_ARROW } from 'tools/keycodes';
 * hotkeys(TablePanel, {
 *  [KEY_UP_ARROW]: 'onPressArrowUp',
 *  [KEY_DOWN_ARROW]: 'onPressArrowDown'
 * })
 */
export { Key }

/**
 * HOC компонент для настройки работы с клавиатурой.
 * Важно! Component не должен быть в другой оберетке, требуется компонент в котором буду методы из config
 * @param BaseComponent - компонент который надо обернуть и в котором находятся методы
 * @param {object} config - мапа вида {KEY_CODE: METHOD_NAME}
 * @example hotkeys(Component, confg)
 */
export function hotkeys(BaseComponent, config) {
    class HOC extends React.Component {
        constructor(props) {
            super(props)
            this.onKeyDown = this.onKeyDown.bind(this)
        }

        onKeyDown(e) {
            const callback = config[e.keyCode]
            this.checkAndCall(callback, e)
        }

        checkAndCall(methodName, args) {
            const cmp = this.baseComponent
            if (cmp && typeof cmp[methodName] === 'function') {
                cmp[methodName](args)
            }
        }

        render() {
            return (
                <div tabIndex="-1" onKeyDown={this.onKeyDown}>
                    <BaseComponent
                        ref={(cmp) => {
                            this.baseComponent = cmp
                        }}
                        {...this.props}
                    />
                </div>
            )
        }
    }

    if (process.env.NODE_ENV !== 'production') {
        return setDisplayName(wrapDisplayName(BaseComponent, 'hotkeys'))(HOC)
    }

    return HOC
}
