import InputText from './InputText/InputText'
import Checkbox from './Checkbox/CheckboxControl'
import DatePicker from './DatePicker/DatePicker'
import DateInterval from './DatePicker/DateInterval'
// eslint-disable-next-line import/no-named-as-default
import PasswordInput from './PasswordInput/PasswordInput'
import CheckboxGroup from './CheckboxGroup/CheckboxGroupControl'
import CodeEditor from './CodeEditor/CodeEditor'
import Html from './Html/Html'
import InputMask from './InputMask/InputMask'
// eslint-disable-next-line import/no-named-as-default
import InputNumber from './InputNumber/InputNumber'
import InputSelectContainer from './InputSelect/InputSelectContainer'
// eslint-disable-next-line import/no-named-as-default
import InputSelectTreeContainer from './InputSelectTree/InputSelectTreeContainer'
import N2OSelectContainer from './N2OSelect/N2OSelectContainer'
import TextEditor from './TextEditor/TextEditor'
import RadioGroupControl from './RadioGroup/RadioGroupControl'
import { SelectContainer } from './Select/SelectContainer'
import OutputText from './Output/OutputText'
import TextArea from './TextArea/TextArea'
import InputHidden from './InputHidden/InputHidden'
import ButtonUploader from './FileUploader/ButtonUploader'
import DropZone from './FileUploader/DropZone'
import Switch from './Switch/Switch'
// eslint-disable-next-line import/no-named-as-default
import InputMoney from './InputMoney/InputMoney'
import SliderContainer from './Slider/Slider'
import Rating from './Rating/Rating'
import Pills from './Pills'
// eslint-disable-next-line import/no-named-as-default
import AutoComplete from './AutoComplete/AutoComplete'
// eslint-disable-next-line import/no-named-as-default
import CodeViewer from './CodeViewer/CodeViewer'
import ProgressControl from './ProgressControl/ProgressControl'
import ImageUploader from './ImageUploader/ImageUploader'
import OutputList from './OutputList/OutputList'
import { NumberPicker } from './NumberPicker/NumberPicker'
import TimePicker from './TimePicker/TimePicker'

export default {
    InputText,
    OutputText,
    Checkbox,
    DatePicker,
    DateInterval,
    PasswordInput,
    CheckboxGroup,
    CodeEditor,
    CodeViewer,
    Html,
    InputMask,
    InputNumber,
    InputSelect: InputSelectContainer,
    InputSelectTree: InputSelectTreeContainer,
    N2OSelect: N2OSelectContainer,
    RadioGroup: RadioGroupControl,
    Select: SelectContainer,
    TextEditor,
    TextArea,
    InputHidden,
    ButtonUploader,
    DropZone,
    Switch,
    InputMoney,
    Slider: SliderContainer,
    Rating,
    Pills,
    AutoComplete,
    ProgressControl,
    ImageUploader,
    OutputList,
    NumberPicker,
    TimePicker,
}
