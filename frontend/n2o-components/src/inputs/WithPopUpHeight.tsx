import React, { ComponentType, createRef } from 'react'
import isEqual from 'lodash/isEqual'

import { Props as ISelectProps, PopUpProps } from './InputSelect/types'
import { Props as SelectProps } from './Select/types'
import { DEFAULT_POPUP_HEIGHT, MEASURE } from './InputSelect/constants'

export function WithPopUpHeight(Component: ComponentType<(ISelectProps | SelectProps) & PopUpProps>) {
    return class Wrapper extends React.Component<ISelectProps | SelectProps, { popUpMaxHeight: number }> {
        popUpItemRef: PopUpProps['popUpItemRef']

        constructor(props: ISelectProps | SelectProps) {
            super(props)
            this.state = { popUpMaxHeight: DEFAULT_POPUP_HEIGHT }
            this.popUpItemRef = createRef()
        }

        componentDidUpdate(prevProps: ISelectProps) {
            const { popUpMaxHeight } = this.state
            const { size, count } = this.props

            // контроль макс высоты и скрола с подгрузкой данных
            const overSize = count !== undefined && size !== undefined && count > 0 && count > size

            if (popUpMaxHeight && ((popUpMaxHeight < DEFAULT_POPUP_HEIGHT) || !overSize)) { return }

            const { options } = this.props

            if (!isEqual(options, prevProps.options)) {
                const popUpItem = this.popUpItemRef?.current

                if (!popUpItem) { return }

                const popUpItemHeight = popUpItem.offsetHeight
                const calculatedMaxHeight = size !== undefined ? size * popUpItemHeight - 10 : -1

                if (calculatedMaxHeight > DEFAULT_POPUP_HEIGHT) {
                    this.setState({ popUpMaxHeight: DEFAULT_POPUP_HEIGHT })

                    return
                }

                this.setState({ popUpMaxHeight: calculatedMaxHeight })
            }
        }

        render() {
            const { popUpMaxHeight } = this.state
            const popUpStyle = { maxHeight: `${popUpMaxHeight}${MEASURE}` }

            return <Component {...this.props} popUpStyle={popUpStyle} popUpItemRef={this.popUpItemRef} />
        }
    }
}
