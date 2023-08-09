import { MarkdownFieldMappers } from '../helpers'

import { N2oButton } from './N2oButton'

export const N2O_BUTTON_TAG = 'n2o-button'

/* прим.  markdown тэг <n2o-button />  использует компонент N2oButton */
export const defaultMarkdownFieldMappers: MarkdownFieldMappers = {
    [N2O_BUTTON_TAG]: N2oButton,
}
