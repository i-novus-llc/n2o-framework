import BlankFieldset from './BlankFieldset'
import TitleFieldset from './TitleFieldset/TitleFieldset'
import CollapseFieldSet from './CollapseFieldset/CollapseFieldSet'
import LineFieldset from './LineFieldset/LineFieldset'
import MultiFieldset from './MultiFieldset/MultiFieldset'

export { default as StandardFieldset } from './BlankFieldset'
export { default as TitleFieldset } from './TitleFieldset/TitleFieldset'
export { default as CollapseFieldSet } from './CollapseFieldset/CollapseFieldSet'
export { default as LineFieldset } from './LineFieldset/LineFieldset'
export { default as MultiFieldset } from './MultiFieldset/MultiFieldset'

export default {
    StandardFieldset: BlankFieldset,
    TitleFieldset,
    CollapseFieldset: CollapseFieldSet,
    LineFieldset,
    MultiFieldset,
}
