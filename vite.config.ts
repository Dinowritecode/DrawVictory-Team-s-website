import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        AutoImport({resolvers: [ElementPlusResolver()],}),
        Components({resolvers: [ElementPlusResolver()],}),
    ],
    server: {
        port: 82,
        host: '0.0.0.0',
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
            },
            '/data': {
                target: 'http://localhost:8080',
                changeOrigin: true,
            },
            '/article': {
                target: 'http://localhost:8080',
                changeOrigin: true,
            }
        }
    }
})
