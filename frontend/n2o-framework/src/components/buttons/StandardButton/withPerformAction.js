import { compose, mapProps } from 'recompose'

import withActionButton from '../withActionButton'
import mappingProps from '../Simple/mappingProps'

export default compose(
    withActionButton({
        onClick: (e, props) => {
            props.dispatch(props.action)
        },
    }),
    mapProps(props => mappingProps(props)),
)
