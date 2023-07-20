import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import * as path from "path";
import cssnano from 'cssnano';
import autoprefixer from 'autoprefixer';
import legacy from '@vitejs/plugin-legacy'

// https://vitejs.dev/config/
export default defineConfig({
    resolve: {
        alias: {
            '@': path.resolve(__dirname, 'src')
        }
    },
    plugins: [
        vue(),
        AutoImport({
            resolvers: [ElementPlusResolver()],
            imports: [
                'vue',
            ],
            dts: true
        }),
        Components({resolvers: [ElementPlusResolver()],}),
        legacy({
            targets: ['chrome 52'],
            additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
            renderLegacyChunks: true,
            polyfills: [
                'es.symbol',
                'es.promise',
                'es.promise.finally',
                'es/map',
                'es/set',
                'es.array.filter',
                'es.array.for-each',
                'es.array.flat-map',
                'es.object.define-properties',
                'es.object.define-property',
                'es.object.get-own-property-descriptor',
                'es.object.get-own-property-descriptors',
                'es.object.keys',
                'es.object.to-string',
                'web.dom-collections.for-each',
                'esnext.global-this',
                'esnext.string.match-all'
            ]
        }),
    ],
    css: {
        preprocessorOptions: {
            less: {
                javascriptEnabled: true,
            }
        },
        postcss: {
            plugins: [
                cssnano,
                autoprefixer,
            ]
        },
    },
    server: {
        port: 80,
        host: '0.0.0.0',
        proxy: {
            '/api': {
                target: 'http://localhost:8081',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
            },
            '/data': {
                target: 'http://localhost:8080',
                changeOrigin: true,
            }
        }
    },
    build: {
        chunkSizeWarningLimit: 1000,
        rollupOptions: {
            output: {
                manualChunks: {
                    vue: ['vue'],
                    vuex: ['vuex'],
                    axios: ['axios'],
                    'vue-router': ['vue-router'],
                    'element-plus': ['element-plus']
                },
                // 用于从入口点创建的块的打包输出格式[name]表示文件名,[hash]表示该文件内容hash值
                entryFileNames: 'js/[name].[hash].js',
                // 用于命名代码拆分时创建的共享块的输出命名
                chunkFileNames: 'js/[name].[hash].js',
                // 用于输出静态资源的命名，[ext]表示文件扩展名
                assetFileNames: '[ext]/[name].[hash].[ext]',
            }
        }
    }
})
