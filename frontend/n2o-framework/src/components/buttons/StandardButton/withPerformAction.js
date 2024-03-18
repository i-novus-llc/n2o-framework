import { compose } from 'recompose'

import withActionButton from '../withActionButton'

export const withPerformAction = compose(
    withActionButton({
        onClick: (e, props) => {
            const { action, onClick, dispatch, actionCallback } = props

            if (actionCallback) {
                actionCallback()
            }

            if (action) {
                dispatch(action)
            }

            if (onClick) {
                onClick(e)
            }
        },
    }),
)

export default withPerformAction
