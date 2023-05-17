// @ts-ignore ignore import error from js file
import propsResolver from './propsResolver'

type modelType = object

type buttonType = { hint: string, label: string }
type buttonsType = buttonType[]
type toolbarElementType = { buttons: buttonsType }
type toolbarType = toolbarElementType[]

function resolveButtonsText(buttons: buttonsType = [], model: modelType = {}) {
    return buttons.map((button: buttonType) => {
        const { hint, label } = button

        return { ...button, ...propsResolver({ hint, label }, model) }
    })
}

/* резолвит buttons text props в StandardWidget */
export function resolveToolbarText(toolbar: toolbarType = [], model: modelType = {}) {
    return toolbar.map(({ buttons, ...rest }) => ({ buttons: resolveButtonsText(buttons, model), ...rest }))
}
