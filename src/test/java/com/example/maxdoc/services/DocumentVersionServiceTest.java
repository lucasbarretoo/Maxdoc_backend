package com.example.maxdoc.services;

import com.example.maxdoc.entities.Document;
import com.example.maxdoc.entities.DocumentVersion;
import com.example.maxdoc.respositories.DocumentVersionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class DocumentVersionServiceTest {

    @Mock
    private DocumentVersionRepository documentVersionRepository;

    @InjectMocks
    private DocumentVersionService documentVersionService;

    @Captor
    private ArgumentCaptor<DocumentVersion> documentVersionArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @Nested
    class saveDocumentVersion {

        @Test
        @DisplayName("Deve salvar versão de documento com sucesso")
        void shouldSaveDocumentVersion() {
            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            DocumentVersion documentVersion = new DocumentVersion(
                1,
                "DT",
                "Documento Teste",
                "Minuta"
            );

            documentVersion.setDocument(document);

            doReturn(documentVersion).when(documentVersionRepository)
                .save(documentVersionArgumentCaptor.capture());

            DocumentVersion newDocVersion = documentVersionService.save(documentVersion);

            assertNotNull(newDocVersion);

            var documentSaved = documentVersionArgumentCaptor.getValue();

            assertEquals(documentVersion.getId(), documentSaved.getId());
            assertEquals(documentVersion.getVersion(), documentSaved.getVersion());
            assertEquals(documentVersion.getAbbrev(), documentSaved.getAbbrev());
            assertEquals(documentVersion.getPhase(), documentSaved.getPhase());
            assertEquals(documentVersion.getDescription(), documentSaved.getDescription());
            assertEquals(documentVersion.getDocument().getId(), documentSaved.getDocument().getId());
        }

        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            DocumentVersion documentVersion = new DocumentVersion(
                    1,
                    "DT",
                    "Documento Teste",
                    "Minuta"
            );

            documentVersion.setDocument(document);

            doThrow(new RuntimeException()).when(documentVersionRepository).save(any());

            assertThrows(RuntimeException.class, () -> documentVersionService.save(documentVersion));
        }
    }

    @Nested
    class updateDocumentVersion {

        @Test
        @DisplayName("Deve atualizar versão de documento")
        void shouldUpdateDocumentVersion() {

            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            DocumentVersion documentVersion = new DocumentVersion(
                    1,
                    "DT",
                    "Documento Teste",
                    "Minuta"
            );

            documentVersion.setDocument(document);

            doReturn(Optional.of(documentVersion)).when(documentVersionRepository).findById(integerArgumentCaptor.capture());

            doReturn(documentVersion).when(documentVersionRepository).save(documentVersionArgumentCaptor.capture());

            DocumentVersion docVersionUpdated = documentVersionService.update(documentVersion.getId(), documentVersion);

            assertNotNull(docVersionUpdated);

            assertEquals(documentVersion.getId(), docVersionUpdated.getId());
            assertEquals(documentVersion.getPhase(), docVersionUpdated.getPhase());
        }


        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            DocumentVersion documentVersion = new DocumentVersion(
                    1,
                    "DT",
                    "Documento Teste",
                    "Minuta"
            );

            documentVersion.setDocument(document);

            doReturn(Optional.of(documentVersion)).when(documentVersionRepository).findById(integerArgumentCaptor.capture());

            doThrow(new RuntimeException()).when(documentVersionRepository).save(documentVersionArgumentCaptor.capture());

            assertThrows(RuntimeException.class, () -> documentVersionService.update(documentVersion.getId(), documentVersion));

        }

    }

    @Nested
    class getOrderedVersions {

        @Test
        @DisplayName("Deve retornar todas as vesões de um documento ordenado pela versão")
        void shouldReturnOrderedVersions() {
            Random random = new Random();

            Document document = new Document(
                    random.nextInt() * 10,
                    "title test"
            );

            DocumentVersion documentVersion = new DocumentVersion(
                    1,
                    "DT",
                    "Documento Teste",
                    "Minuta"
            );

            documentVersion.setDocument(document);

            DocumentVersion documentVersion2 = new DocumentVersion(
                    2,
                    "DT",
                    "Documento Teste descricao alterada",
                    "Minuta"
            );

            documentVersion2.setDocument(document);

            List<DocumentVersion> documentVersions = List.of(documentVersion, documentVersion2);

            doReturn(documentVersions).when(documentVersionRepository)
                    .findVersionsByDocumentIdOrderByVersion(integerArgumentCaptor.capture());

            List<DocumentVersion> docVersionsList = documentVersionService.getOrderedVersionsByDocumentId(document.getId());

            assertEquals(documentVersions.size(), docVersionsList.size());
        }

        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            doThrow(new RuntimeException()).when(documentVersionRepository).findVersionsByDocumentIdOrderByVersion(any());

            assertThrows(RuntimeException.class, () -> documentVersionService.getOrderedVersionsByDocumentId(any()));
        }
    }
}