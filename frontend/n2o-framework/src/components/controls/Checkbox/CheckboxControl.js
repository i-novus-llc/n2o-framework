import { compose, withHandlers, defaultProps } from 'recompose'

import CheckboxN2O from './CheckboxN2O'

export default compose(
    defaultProps({
        defaultUnchecked: 'null',
    }),
    withHandlers({
        onChange: props => (event) => {
            const value = event.nativeEvent.target.checked
            const defaultUnchecked =
        props.defaultUnchecked === 'false' ? false : null

            props.onChange(!value ? defaultUnchecked : value)
        },
    }),
)(CheckboxN2O)
