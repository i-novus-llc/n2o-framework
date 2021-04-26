import widgetContainer from '../WidgetContainer'

import Wireframe from './Wireframe'

export default widgetContainer({
    mapProps: props => ({
        title: props.title,
        className: props.className,
        height: props.height,
    }),
})(Wireframe)
