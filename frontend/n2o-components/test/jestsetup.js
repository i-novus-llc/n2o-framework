// Глобал enzyme
import Enzyme, { shallow, render, mount } from 'enzyme'
import Adapter from '@wojtekmaj/enzyme-adapter-react-17'
import registerRequireContextHook from 'babel-plugin-require-context-hook/register'

registerRequireContextHook()

Enzyme.configure({ adapter: new Adapter() })

global.shallow = shallow
global.render = render
global.mount = mount

console.error = (message) => {
    // throw new Error(message);
}
