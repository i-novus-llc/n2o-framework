import flowRight from 'lodash/flowRight'

import { withActionButton } from '../withActionButton'

export const withPerformAction = flowRight(
    withActionButton({
        onClick: (e, props) => {
            const { action, onClick, dispatch, actionCallback } = props || {}

            if (actionCallback) {
                actionCallback()
            }

            if (action && dispatch) {
                dispatch(action)
            }

            if (onClick) {
                onClick(e)
            }
        },
    }),
)

export default withPerformAction
